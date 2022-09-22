/*
  Warnings:

  - You are about to drop the column `nombre` on the `test` table. All the data in the column will be lost.
  - Added the required column `content` to the `Test` table without a default value. This is not possible if the table is not empty.
  - Added the required column `title` to the `Test` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `test` DROP COLUMN `nombre`,
    ADD COLUMN `content` VARCHAR(191) NOT NULL,
    ADD COLUMN `title` VARCHAR(191) NOT NULL;
