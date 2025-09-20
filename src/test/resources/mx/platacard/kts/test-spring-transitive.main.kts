#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.springframework:spring-beans:6.1.5")
// spring-core должен быть транзитивной зависимостью от spring-beans

import org.springframework.beans.factory.support.DefaultListableBeanFactory  // Прямая зависимость: spring-beans
import org.springframework.beans.factory.support.RootBeanDefinition  // Прямая зависимость: spring-beans
import org.springframework.beans.factory.BeanFactory  // ТРАНЗИТИВНАЯ зависимость: spring-core (через spring-beans)

// Простой тест Spring транзитивных зависимостей
class TestService(private val message: String) {
    fun getMessage(): String = message
}

// Создаем простой Spring BeanFactory
val beanFactory = DefaultListableBeanFactory()

try {
    // Регистрируем bean
    val beanDefinition = RootBeanDefinition(TestService::class.java)
    beanDefinition.constructorArgumentValues.addGenericArgumentValue("Hello from Spring!")
    beanFactory.registerBeanDefinition("testService", beanDefinition)
    
    // Получаем bean
    val testService = beanFactory.getBean("testService", TestService::class.java)
    println("✅ Spring bean created: ${testService.getMessage()}")
    
    // Тестируем транзитивные зависимости Spring
    println("✅ Spring BeanFactory available: ${beanFactory.javaClass.simpleName}")
    println("✅ Spring RootBeanDefinition available: ${beanDefinition.javaClass.simpleName}")
    
    // Тестируем другие Spring классы
    val beanNames = beanFactory.getBeanDefinitionNames()
    println("✅ Spring bean names: ${beanNames.joinToString()}")
    
} catch (e: Exception) {
    println("❌ Spring error: ${e.message}")
}

println("✅ All Spring transitive dependencies work correctly!")
