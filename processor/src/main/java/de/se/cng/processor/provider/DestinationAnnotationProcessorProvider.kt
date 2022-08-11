package composektx.navigationgenerator.processor.provider

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import composektx.navigationgenerator.processor.processor.DestinationAnnotationProcessor
import composektx.navigationgenerator.processor.visitors.FunctionDeclarationVisitor

@AutoService(SymbolProcessorProvider::class)
class DestinationAnnotationProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment) = DestinationAnnotationProcessor(
        options = environment.options,
        codeGenerator = environment.codeGenerator,
        logger = environment.logger,
        //functionDeclarationVisitor = FunctionDeclarationVisitor()
    )

}