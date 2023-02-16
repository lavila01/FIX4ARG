-- fix.cotizaciones_rofex_fix definition

CREATE TABLE `cotizaciones_rofex_fix`
(
    `symbol`         varchar(191) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `data`           text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `fecha`          datetime                                                NOT NULL,
    `fix_message`    text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `categoria`      varchar(191) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `ultimo_operado` float                                                   DEFAULT NULL,
    `variacion`      float                                                   DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;