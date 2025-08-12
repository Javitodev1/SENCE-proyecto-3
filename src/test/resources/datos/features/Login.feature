Feature: Login
Para verificar que el usuario pueda iniciar sesión correctamente
Scenario: Inicio de sesión válido
Given el usuario está en la página de login
When ingresa "pedrito123" y "Pedrito123!"
Then se muestra la pantalla principal

Scenario: Inicio de sesión inválido
    Given el usuario está en la página de login
    When ingresa "pedrito123" y "ContraseñaIncorrecta!"
    And hace clic en el botón de inicio de sesión
    But las credenciales no son válidas
    Then se muestra un mensaje de error indicando que las credenciales son incorrectas

