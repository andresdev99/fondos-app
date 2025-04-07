
# 📌 Datos de la Aplicación
Aplicacion de Fondos [Link](http://ec2-44-202-242-85.compute-1.amazonaws.com:5174/)
## Fondos Disponibles:

| Id | Nombre del Fondo            | Monto mínimo | Categoría |
|----|-----------------------------|--------------|-----------|
| 1  | FPV_EL CLIENTE_RECAUDADORA  | $75,000 COP  | FPV       |
| 2  | FPV_EL CLIENTE_ECOPETROL    | $125,000 COP | FPV       |
| 3  | DEUDAPRIVADA                | $50,000 COP  | FIC       |
| 4  | FDO-ACCIONES                | $250,000 COP | FIC       |
| 5  | FPV_EL CLIENTE_DINAMICA     | $100,000 COP | FPV       |

## Reglas de Negocio:
- El monto inicial del usuario es de **$500,000 COP**.
- Cada transacción genera un **identificador único**.
- Al desvincularse de un fondo, el valor invertido es retornado al cliente.
- Si el saldo es insuficiente, se mostrará el mensaje:
  ```
  No tiene saldo disponible para vincularse al fondo <Nombre del fondo>.
  ```

## Funcionalidades Principales:
- ✅ **Suscripción a Fondos:** El cliente puede abrir nuevas inversiones.
- ✅ **Cancelación de Fondos:** El cliente puede retirarse de fondos existentes.
- ✅ **Historial de Transacciones:** Visualización de aperturas y cancelaciones.
- ✅ **Notificaciones:** Envío de email, SMS aún no tiene cobertura.

## 🚀 Tecnologías Usadas:
- **Backend:** Java Spring Boot
- **Frontend:** ReactJS
- **Base de Datos:** DynamoDB
- **Infraestructura y Despliegue:** AWS CloudFormation
