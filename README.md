# 🧪 Proyecto BDD con Java, Selenium y Cucumber

## Integrantes
- Leslie Aguayo
- Jose Astudillo
- Javier Fuentes

Este proyecto implementa pruebas automatizadas para una aplicación web utilizando la metodología **Desarrollo Conducido por Comportamiento (BDD)**. Se emplea **Java**, **Cucumber**, **Selenium WebDriver** y **TestNG**, siguiendo las buenas prácticas de organización, trazabilidad y colaboración entre QA, Desarrollo y Negocio.

## 📌 Objetivo

Diseñar y automatizar escenarios de prueba que validen los flujos de **login** y **registro** de usuarios, asegurando que el comportamiento de la aplicación cumpla con los requerimientos funcionales definidos.

## 🗂️ Estructura del Proyecto
```bash
.
├── pom.xml
├── pom.xml.linux
├── pom.xml.window
├── README.md
├── src
│   └── test
│       ├── java
│       │   ├── hooks
│       │   │   └── Hooks.java
│       │   ├── runners
│       │   │   └── TestRunner.java
│       │   ├── steps
│       │   │   ├── StepsLogin.java
│       │   │   └── StepsRegistro.java
│       │   └── utils
│       │       └── DriverFactory.java
│       └── resources
│           └── features
│               ├── Login.feature
│               └── Registro.feature
└── testng.xml
```

## 🧪 Escenarios Implementados

### 🔐 Login (`Login.feature`)
- Validación de inicio de sesión con credenciales válidas e inválidas.
- Uso de `Scenario Outline` con múltiples combinaciones de datos.
- Verificación de redirección a `/profile` o aparición de mensaje de error.

### 📝 Registro (`Registro.feature`)
- Registro exitoso con datos completos.
- Registro inválido con campos requeridos incompletos.
- Validación de modal de éxito y campos marcados en rojo.

## 🧩 Tecnologías y Librerías

- **Java 11**
- **Cucumber 7.27.0**
- **Selenium WebDriver 4.34.0**
- **WebDriverManager 6.2.0**
- **Apache POI (opcional)**
- **TestNG**
- **Maven**

## 🧵 Organización BDD

- ✅ **Gherkin**: Escenarios redactados con `Given`, `When`, `Then`, `And`, `But`.
- 🧪 **Hooks**: Inicialización y cierre del navegador con `@Before` y `@After`.
- 🏷️ **Tags**: Uso de `@login`, `@register` para filtrar pruebas.
- 🔁 **Scenario Outline**: Parametrización de datos en login.
- 📋 **Reportes**: Generación automática en HTML, JSON y XML.

## 🧾 Trazabilidad

Cada escenario está vinculado a una historia de usuario funcional. Los reportes generados incluyen el estado (`Passed`, `Failed`, `Skipped`) y permiten validar la cobertura de requerimientos.

## ▶️ Ejecución

Para correr los tests:

```bash
mvn test
```

O bien, filtrar por tag:
```bash
mvn test -Dcucumber.filter.tags="@login"
```

## 📊 Reportes

Se generan automáticamente en la carpeta target/:

    cucumber-report.html

    cucumber.json

    cucumber.xml

## 📚 Referencias

- [Cucumber Docs](https://cucumber.io/docs/)
- [Selenium WebDriver](https://www.selenium.dev/documentation/)
- Manuales L1–L6 del módulo BDD

## 📋 Reporte de Ejecución

| Historia de Usuario | Escenario                                | Tags                        | Estado  |
|---------------------|-------------------------------------------|-----------------------------|---------|
| HU-001              | Validar inicio de sesión con credenciales | @login, @SmokeTest          | Passed  |
| HU-001              | Validar inicio de sesión con credenciales | @login, @SmokeTest          | Passed  |
| HU-001              | Validar inicio de sesión con credenciales | @login, @SmokeTest          | Passed  |
| HU-002              | Registro válido                           | @register                   | Passed  |
| HU-003              | Registro inválido                         | @register, @Regression      | Passed  |
