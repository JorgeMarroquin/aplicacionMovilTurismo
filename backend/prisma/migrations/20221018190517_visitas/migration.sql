-- CreateTable
CREATE TABLE `Visitas` (
    `lugarId` INTEGER NOT NULL,
    `usuarioId` INTEGER NOT NULL,
    `visita` DATETIME(3) NOT NULL,

    UNIQUE INDEX `Visitas_lugarId_usuarioId_key`(`lugarId`, `usuarioId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `Visitas` ADD CONSTRAINT `Visitas_lugarId_fkey` FOREIGN KEY (`lugarId`) REFERENCES `Lugares`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `Visitas` ADD CONSTRAINT `Visitas_usuarioId_fkey` FOREIGN KEY (`usuarioId`) REFERENCES `Usuarios`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
