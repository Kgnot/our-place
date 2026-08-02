package org.our_place.common.shared.service;

import org.jspecify.annotations.NonNull;
import org.our_place.common.shared.SharedApi;
import org.our_place.common.shared.SharedDomain;
import org.our_place.common.shared.dto.SharedItemDto;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArchitectureScannerService {

    public List<SharedItemDto> scanSharedItems() {
        List<SharedItemDto> sharedItems = new ArrayList<>();

        // escaneamos el paquete
        String basePackage = "org.our_place";
        //configuramos el escáner
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(@NonNull AnnotatedBeanDefinition beanDefinition) {
                // Queremos interfaces y clases concretas
                return true;
            }
        };

        scanner.addIncludeFilter(new AnnotationTypeFilter(SharedApi.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(SharedDomain.class));

        for (var beanDefinition : scanner.findCandidateComponents(basePackage)) {
            String className = beanDefinition.getBeanClassName();
            try {
                assert className != null;
                Class<?> clazz = ClassUtils.forName(className, getClass().getClassLoader());

                String type = "";
                String description = "";

                if (clazz.isAnnotationPresent(SharedApi.class)) {
                    type = "API";
                    description = clazz.getAnnotation(SharedApi.class).description();
                } else if (clazz.isAnnotationPresent(SharedDomain.class)) {
                    type = "DOMAIN";
                    description = clazz.getAnnotation(SharedDomain.class).description();
                }

                sharedItems.add(new SharedItemDto(type, clazz.getSimpleName(), clazz.getPackageName(), description));

            } catch (ClassNotFoundException e) {
                // Ignorar si no se puede cargar
            }
        }

        // Ordenar por tipo y nombre para que se vea bonito
        sharedItems.sort((a, b) -> a.type().compareTo(b.type()) == 0
                ? a.className().compareTo(b.className())
                : a.type().compareTo(b.type()));

        return sharedItems;
    }
}
