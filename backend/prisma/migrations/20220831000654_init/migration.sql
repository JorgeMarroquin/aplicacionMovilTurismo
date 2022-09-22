-- CreateTable
CREATE TABLE `Usuarios` (
    `usuarioId` INTEGER NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(191) NOT NULL,
    `apellido` VARCHAR(191) NOT NULL,
    `correo` VARCHAR(191) NOT NULL,
    `nacionalidad` VARCHAR(191) NOT NULL,
    `telefono` INTEGER NOT NULL,
    `password` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`usuarioId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
