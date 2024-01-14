package com.example.annotation
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationContext
import com.android.build.api.instrumentation.InstrumentationParameters
import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Modifier.*
import com.google.devtools.ksp.symbol.KSFile
import org.gradle.api.provider.Property


import org.objectweb.asm.*
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassWriter.COMPUTE_FRAMES
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.objectweb.asm.Opcodes.ASM9
import org.objectweb.asm.Opcodes.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class ThreadCheckerProcessor(
    private val logger: KSPLogger,
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {
    @Suppress("NewApi")
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val annotatedFunctions = resolver.getSymbolsWithAnnotation(ThreadChecker::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()

        for (function in annotatedFunctions) {
            logger.warn("Found @ThreadChecker annotation on method: ${function.simpleName.asString()} in class: ${function.containingFile?.javaClass?.simpleName}")

            val containingFile = function.containingFile ?: continue
            val classKSClassDeclaration = containingFile.declarations.firstOrNull { it is KSClassDeclaration && it.declarations.contains(function) } ?: continue
            val containingClass = classKSClassDeclaration.javaClass.simpleName
            val methodName = function.simpleName.asString()

           val threadName = function.annotations.find { it.shortName.asString() == "ThreadChecker" }?.arguments?.firstOrNull { it.name?.asString() == "threadName" }?.value
            if (threadName != null) {
                logger.warn("Found @ThreadChecker annotation on method: ${function.simpleName} in class: $containingClass")

                // Read the original class bytecode
                val classFile = File(containingFile.filePath)
                val classBytes = Files.readAllBytes(Paths.get(classFile.absolutePath))

                // Create a ClassReader to read the original bytecode
                val classReader = ClassReader(classBytes)

                // Create a ClassWriter to generate modified bytecode
                val classWriter = ClassWriter(classReader, COMPUTE_FRAMES or COMPUTE_MAXS)

                // Create a ClassVisitor to visit and modify the bytecode
                val classVisitor = object : ClassVisitor(ASM9, classWriter) {
                    override fun visitMethod(
                        access: Int,
                        name: String?,
                        descriptor: String?,
                        signature: String?,
                        exceptions: Array<out String>?
                    ): MethodVisitor {
                        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)

                        if (name == methodName ) {
                            // Generate the ASM code to check the thread name
                            val asmCode = """
                                |if (!Thread.currentThread().name.equals("$threadName")) {
                                |    throw new IllegalStateException("Method $methodName must be called on thread $threadName");
                                |}
                            """.trimMargin()

                            // Create a MethodVisitor to visit and modify the method bytecode
                            return object : MethodVisitor(ASM9, methodVisitor) {
                                override fun visitCode() {
                                    super.visitCode()

                                    // Insert the ASM code at the beginning of the method
                                    visitLdcInsn(threadName)
                                    visitMethodInsn(
                                        INVOKESTATIC,
                                        "java/lang/Thread",
                                        "currentThread",
                                        "()Ljava/lang/Thread;",
                                        false
                                    )
                                    visitInsn(DUP)
                                    visitMethodInsn(
                                        INVOKEVIRTUAL,
                                        "java/lang/Thread",
                                        "getName",
                                        "()Ljava/lang/String;",
                                        false
                                    )
                                    visitMethodInsn(
                                        INVOKEVIRTUAL,
                                        "java/lang/String",
                                        "equals",
                                        "(Ljava/lang/Object;)Z",
                                        false
                                    )
                                    visitJumpInsn(IFNE, null)
                                    visitTypeInsn(NEW, "java/lang/IllegalStateException")
                                    visitInsn(DUP)
                                    visitLdcInsn("Method $methodName must be called on thread $threadName")
                                    visitMethodInsn(
                                        INVOKESPECIAL,
                                        "java/lang/IllegalStateException",
                                        "<init>",
                                        "(Ljava/lang/String;)V",
                                        false
                                    )
                                    visitInsn(ATHROW)
                                }
                            }
                        }

                        return methodVisitor
                    }
                }

                // Visit the class bytecode and generate the modified bytecode
                classReader.accept(classVisitor, EXPAND_FRAMES)

                // Get the modified bytecode
                val modifiedBytes = classWriter.toByteArray()

                // Write the modified bytecode back to the class file
                Files.write(Paths.get(classFile.toURI()), modifiedBytes)
            }
        }

        return emptyList()
    }



    private fun findContainingClass(
        containingFile: KSFile,
        containingClass: KSClassDeclaration
    ): KSClassDeclaration? {
        val packageName = containingFile.packageName.asString()
        val className = containingClass.simpleName.asString()
        return containingFile.declarations
            .filterIsInstance<KSClassDeclaration>()
            .find { it.packageName.asString() == packageName && it.simpleName.asString() == className }
    }



    override fun finish() {}
}



