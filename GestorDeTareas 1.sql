-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gestorTareas1
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gestorTareas1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gestorTareas1` DEFAULT CHARACTER SET utf8 ;
USE `gestorTareas1` ;

-- -----------------------------------------------------
-- Table `gestorTareas1`.`Prioridad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestorTareas1`.`Prioridad` (
  `idPrioridad` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idPrioridad`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestorTareas1`.`Rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestorTareas1`.`Rol` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `nombreRol` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRol`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestorTareas1`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestorTareas1`.`Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(70) NOT NULL,
  `contrasena` VARCHAR(300) NOT NULL,
  `idRol` INT NOT NULL,
  PRIMARY KEY (`idUsuario`),
  INDEX `fk_Usuario_Rol1_idx` (`idRol` ASC) VISIBLE,
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_Rol1`
    FOREIGN KEY (`idRol`)
    REFERENCES `gestorTareas1`.`Rol` (`idRol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestorTareas1`.`Tareas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestorTareas1`.`Tareas` (
  `idTarea` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `idPrioridad` INT NOT NULL,
  `idUsuario` INT NOT NULL,
  PRIMARY KEY (`idTarea`),
  INDEX `fk_Tarea_Prioridad_idx` (`idPrioridad` ASC) VISIBLE,
  INDEX `fk_Tareas_Usuario1_idx` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Tarea_Prioridad`
    FOREIGN KEY (`idPrioridad`)
    REFERENCES `gestorTareas1`.`Prioridad` (`idPrioridad`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Tareas_Usuario1`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `gestorTareas1`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
