package mateusz512.testablelogging.application

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

@AnalyzeClasses(packages = ["mateusz512.testablelogging"])
class ArchitectureTest {

    @ArchTest
    val `domain should not depend on Spring` = ArchRuleDefinition
        .noClasses().that().resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat().resideInAPackage("..springframework..")

}