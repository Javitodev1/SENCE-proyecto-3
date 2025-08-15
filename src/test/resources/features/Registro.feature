@register @HU_002
Feature: Registro
Para verificar que el usuario pueda registrarse correctamente

  Scenario: Registro válido
    Given el usuario está en la página de registro
    When ingresa "Laura", "Pausini", "laura@mail.com", "Female", "9876543210"
    And hace clic en el botón de registro
    Then se muestra un modal de registro exitoso

@register @HU_003 @Regression
  Scenario: Registro inválido
    Given el usuario está en la página de registro
    When ingresa "", "Pausini", "", "Female", "9876543210"
    And hace clic en el botón de registro
    But no completa todos los campos requeridos
    Then se marcan en rojo los campos requeridos incompletos
