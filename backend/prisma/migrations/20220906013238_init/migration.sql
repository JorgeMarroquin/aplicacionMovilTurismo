/*
  Warnings:

  - Added the required column `departamento` to the `Lugares` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `lugares` ADD COLUMN `calificacion` DOUBLE NOT NULL DEFAULT 0,
    ADD COLUMN `departamento` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `usuarios` MODIFY `telefono` VARCHAR(191) NOT NULL;
