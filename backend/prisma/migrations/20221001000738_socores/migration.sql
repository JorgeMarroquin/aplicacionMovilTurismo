-- CreateTable
CREATE TABLE `Scores` (
    `lugarId` INTEGER NOT NULL,
    `usuarioId` INTEGER NOT NULL,
    `score` DOUBLE NOT NULL,

    UNIQUE INDEX `Scores_lugarId_usuarioId_key`(`lugarId`, `usuarioId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `Scores` ADD CONSTRAINT `Scores_lugarId_fkey` FOREIGN KEY (`lugarId`) REFERENCES `Lugares`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `Scores` ADD CONSTRAINT `Scores_usuarioId_fkey` FOREIGN KEY (`usuarioId`) REFERENCES `Usuarios`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
