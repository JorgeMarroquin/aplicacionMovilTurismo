/*
  Warnings:

  - Added the required column `comentario` to the `Comentarios` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `comentarios` ADD COLUMN `comentario` LONGTEXT NOT NULL;
