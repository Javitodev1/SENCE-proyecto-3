@login
Feature: Inicio de sesión

  Scenario Outline: Validar inicio de sesión con credenciales
    Given el usuario está en la página de login
    When ingresa "<usuario>" y "<password>"
    And hace clic en el botón de inicio de sesión
    Then si el resultado es "<resultado>" se muestra la pantalla principal
    But si el resultado es "<resultado>" se muestra un mensaje de error

    Examples:
      | usuario         | password              | resultado       |
      | pedrito123      | Pedrito123!           | exitoso         |
      | Weasta          | Nacho0109#            | exitoso         |
      | invalidUser     | wrongPassword         | fallido         |
