package com.example.annotation

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationContext
import com.android.build.api.instrumentation.InstrumentationParameters
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.gradle.AppPlugin
import com.sun.source.util.Plugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9

/*class AsmPlugin :org.gradle.api.Plugin<Project>{
    override fun apply(project: Project) {
        project.plugins.withType(AppPlugin::class.java){
            val androidComponent = project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
            androidComponent.onVariants {applicationVariant ->
                val taskProvider = project.tasks.register("${applicationVariant.name}ModifyClassTask")
                applicationVariant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
                    .use(taskProvider)
                    .toTransform(
                        ScopedArtifact.CLASSES,
                        ModifyClassTask::allJars,
                        ModifyClassTask::allDirectories,
                        ModifyClassTask::output
                    )


            }
        }



    }

}*/

abstract class ModifyClassTask: DefaultTask(){
    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    // Gradle will set this property with all class directories that available in scope
    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    // Task will put all classes from directories and jars after optional modification into single jar
    @get:OutputFile
    abstract val output: RegularFileProperty


    @TaskAction
    fun modify() {

    }


}


abstract class VisitorFactory: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return AsmVisitor(classContext, nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        TODO("Not yet implemented")
    }

}


class AsmVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor) : ClassVisitor(ASM9) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)


    }
}

