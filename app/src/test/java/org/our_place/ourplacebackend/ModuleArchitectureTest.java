package org.our_place.ourplacebackend;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "org.our_place")
public class ModuleArchitectureTest {

    // Regla 1: Nadie de afuera puede tocar la implementación interna de Identity
    @ArchTest
    static final ArchRule modulos_externos_no_tocan_internals_de_identity =
            noClasses().that().resideOutsideOfPackage("..identity..")
                    .should().accessClassesThat().resideInAPackage("..identity.internal..")
                    .orShould().accessClassesThat().resideInAPackage("..identity.provider..");

    // Regla 2: Solo se pueden importar cosas de la carpeta 'api' de otros módulos
    @ArchTest
    static final ArchRule map_solo_depende_de_api_de_otros =
            classes().that().resideInAPackage("..map..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage(
                            "..map..",
                            "..shared..",
                            "..identity.api..",
                            "java..", "org.springframework.."
                    ).allowEmptyShould(true);
}