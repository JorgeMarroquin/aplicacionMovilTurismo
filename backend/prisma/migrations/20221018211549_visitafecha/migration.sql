/*
  Warnings:

  - You are about to drop the column `visita` on the `visitas` table. All the data in the column will be lost.
  - Added the required column `visitaFecha` to the `Visitas` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `visitas` DROP COLUMN `visita`,
    ADD COLUMN `visitaFecha` DATETIME(3) NOT NULL;
