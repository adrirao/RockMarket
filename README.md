# Rock!Market

## Descripción

RockMarket es una aplicación de comercio electrónico para Android que permite a los usuarios
explorar productos, escanear códigos QR para ver detalles de productos y realizar compras. La
aplicación implementa una arquitectura moderna y utiliza diversas tecnologías contemporáneas de
desarrollo Android.

## Tecnologías Utilizadas

### Arquitectura y Patrones

- **Clean Architecture**: Separación de capas (Presentación, Dominio, Datos)
- **MVVM (Model-View-ViewModel)**: Patrón arquitectónico para la capa de presentación
- **Repository Pattern**: Para abstraer las fuentes de datos
- **Use Cases**: Para implementar la lógica de negocio
- **Dependency Injection**: Utilizando Hilt para la inyección de dependencias

### Framework y Bibliotecas

- **Kotlin**: Lenguaje de programación principal
- **Jetpack Components**:
    - **Navigation Component**: Para gestionar la navegación entre pantallas
    - **ViewModel**: Para la gestión del estado y la lógica de presentación
    - **LiveData/Flow**: Para la gestión de datos reactivos
    - **Jetpack Compose**: Para la interfaz de usuario moderna y declarativa (en algunas pantallas)
- **Coroutines**: Para manejar operaciones asíncronas
- **Firebase**:
    - **Firebase Auth**: Para autenticación de usuarios
    - **Google Sign-In**: Para inicio de sesión con Google
- **CameraX**: API de cámara para escanear códigos QR
- **ML Kit**: Para el análisis y reconocimiento de códigos QR
- **Retrofit**: Para realizar llamadas a API REST
- **Glide**: Para carga y caché de imágenes
- **Material Design 3**: Para componentes de UI siguiendo las guías de diseño de Google

### APIs Externas

- **Fake Store API**: Para obtener datos de productos de ejemplo
- **Platzi API**: API alternativa para obtener productos de ejemplo

## Pantallas y Funcionalidades

### 1. Login y Autenticación

- **Ruta**: `app/src/main/java/dev/rao/rockmarket/auth/login/`
- **Funcionalidad**: Permite a los usuarios iniciar sesión con email/contraseña o con Google.
- **Tecnologías específicas**: Firebase Auth, Google Sign-In API.
- **Navegación**: Al iniciar sesión correctamente, navega a la selección de país.

### 2. Selección de País

- **Ruta**: `app/src/main/java/dev/rao/rockmarket/country/`
- **Funcionalidad**: Permite al usuario seleccionar su país para personalizar la experiencia.
- **Datos**: Lista pre-configurada de países con sus banderas y símbolos de moneda.
- **Navegación**: Al seleccionar un país, navega a la pantalla principal.

### 3. Pantalla Principal (Home)

- **Ruta**: `app/src/main/java/dev/rao/rockmarket/home/`
- **Funcionalidad**: Muestra un listado de productos disponibles para comprar.
- **Tecnologías específicas**: Jetpack Compose para la UI, Retrofit para obtener los datos de la
  API.
- **Componentes UI**: Implementa efectos de Neumorfismo para un diseño moderno.
- **Navegación**: Permite navegar al escáner QR o a los detalles de un producto.

### 4. Escáner QR

- **Ruta**: `app/src/main/java/dev/rao/rockmarket/home/presentation/scanner/`
- **Funcionalidad**: Permite escanear códigos QR para ver directamente los detalles de un producto.
- **Tecnologías específicas**: CameraX, ML Kit para procesamiento de códigos QR.
- **Navegación**: Al escanear un QR válido, navegación directa a la pantalla de detalle del
  producto.

### 5. Detalle de Producto

- **Ruta**: `app/src/main/java/dev/rao/rockmarket/detail_product/`
- **Funcionalidad**: Muestra información detallada del producto seleccionado.
- **Características**: Implementa CollapsingToolbarLayout para una experiencia visual mejorada.
- **Navegación**: Implementa navegación personalizada para siempre volver al listado de productos al
  presionar el botón de retroceso.

### 6. Pantalla de Pago

- **Ruta**: `app/src/main/java/dev/rao/rockmarket/payment/`
- **Funcionalidad**: Formulario de pago para procesar la compra de un producto.
- **Características**: Validación de datos de tarjeta y simulación de procesamiento de pago.
- **Navegación**: Al completar el pago, navegación a la pantalla principal.

## Arquitectura del Proyecto

### Capa de Datos

- **Repositorios**: Implementan interfaces definidas en la capa de dominio.
- **Fuentes de Datos**: API remotas (Fake Store API, Platzi API) y preferencias locales.
- **Mappers**: Conversión entre DTOs y modelos de dominio.

### Capa de Dominio

- **Modelos**: Entidades de negocio como `Product`, `User`, `Country`.
- **Use Cases**: Lógica de negocio específica como `GetProductsUseCase`, `ValidateQrCodeUseCase`.
- **Repositorios (Interfaces)**: Definen contratos para la capa de datos.

### Capa de Presentación

- **ViewModels**: Mantienen el estado y manejan la lógica de UI.
- **States**: Clases selladas para representar diferentes estados de la UI.
- **UI**: Fragmentos tradicionales con ViewBinding y pantallas de Compose.

## Características Destacadas

1. **Interfaz híbrida**: Combina Views tradicionales con Jetpack Compose.
2. **Multi-API**: Soporte para diferentes APIs de productos según el país seleccionado.
3. **Neumorfismo**: Diseño visual moderno para la interfaz de usuario.
4. **Escaneo QR**: Funcionalidad avanzada de escaneo de códigos QR para acceder a productos.
5. **Navegación personalizada**: Control del flujo de navegación entre pantallas.

## Flujo de Navegación

1. Login → Selección de País → Home
2. Home → Detalle de Producto → Pago → Home
3. Home → Escáner QR → Detalle de Producto → Home

## Estructura del Proyecto

La estructura del proyecto sigue principios de modularización por características, donde cada
funcionalidad principal tiene su propio paquete con subpaquetes para datos, dominio y presentación. 