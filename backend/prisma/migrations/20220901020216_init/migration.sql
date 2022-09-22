/*
  Warnings:

  - You are about to drop the `restaurantes` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `restaurantesfavoritos` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropForeignKey
ALTER TABLE `restaurantesfavoritos` DROP FOREIGN KEY `RestaurantesFavoritos_restauranteId_fkey`;

-- DropForeignKey
ALTER TABLE `restaurantesfavoritos` DROP FOREIGN KEY `RestaurantesFavoritos_usuarioId_fkey`;

-- DropTable
DROP TABLE `restaurantes`;

-- DropTable
DROP TABLE `restaurantesfavoritos`;

-- CreateTable
CREATE TABLE `Lugares` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(191) NOT NULL,
    `tipoLugar` ENUM('RESTAURANTE', 'TURISTICO') NOT NULL,
    `latitud` DOUBLE NOT NULL,
    `longitud` DOUBLE NOT NULL,
    `imagen` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `LugaresFavoritos` (
    `lugarId` INTEGER NOT NULL,
    `usuarioId` INTEGER NOT NULL,

    UNIQUE INDEX `LugaresFavoritos_lugarId_usuarioId_key`(`lugarId`, `usuarioId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `LugaresFavoritos` ADD CONSTRAINT `LugaresFavoritos_lugarId_fkey` FOREIGN KEY (`lugarId`) REFERENCES `Lugares`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `LugaresFavoritos` ADD CONSTRAINT `LugaresFavoritos_usuarioId_fkey` FOREIGN KEY (`usuarioId`) REFERENCES `Usuarios`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
