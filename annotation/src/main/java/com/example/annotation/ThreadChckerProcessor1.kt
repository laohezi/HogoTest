package com.example.annotation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes





class ThreadCheckerProcessor1(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationMethods =
            resolver.getSymbolsWithAnnotation(ThreadChecker::class.qualifiedName!!)
                .filterIsInstance<KSFunctionDeclaration>()
        annotationMethods.forEach {
            val visitor = KSPVisitor(codeGenerator, it, logger)
            it.accept(visitor, Unit)
        }
        return emptyList()
    }


}

class KSPVisitor(
    val codeGenerator: CodeGenerator,
    val method: KSFunctionDeclaration,
    private val logger: KSPLogger
) : KSVisitorVoid() {
    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        println("lalala"+function.qualifiedName?.asString())
        val mv = ASMVisitor(codeGenerator, method, logger)
        mv.visitCode()
        mv.visitEnd()

    }
}

class ASMVisitor(
    val codeGenerator: CodeGenerator,
    val method: KSFunctionDeclaration,
    private val logger: KSPLogger
) : MethodVisitor(Opcodes.ASM9) {

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }
    override fun visitCode() {
        super.visitCode()
        val threadName =
            method.annotations.find { it.shortName.asString() == "ThreadChecker" }?.arguments?.get(0)?.value
        visitFieldInsn(
            Opcodes.GETSTATIC,
            "java/lang/Thread",
            "currentThread",
            "Ljava/lang/Thread;"
        )
        visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/Thread",
            "getName",
            "()Ljava/lang/String;",
            false
        )
        visitLdcInsn(threadName)
        visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/String",
            "equals",
            "(Ljava/lang/Object;)Z",
            false
        )
        val labelEnd = Label()
        visitJumpInsn(Opcodes.IFEQ, labelEnd)
        //log error
        visitLdcInsn("Method ${method.simpleName.asString()} must be called from thread $threadName but was called from ${Thread.currentThread().name}")
        visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        )

        // Continue with original method code
        visitLabel(labelEnd)


    }


}