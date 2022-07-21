package cn.xiaobinbin.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class CodeGeneration {

    public static final String path = FileUtils.readYml("gencode.outputUrl") + "/src/main/java";

    public static final String packageName = FileUtils.readYml("gencode.packageName");

    public static void run() {
        AutoGenerator generator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = getGlobalConfig();
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        getFileName(generator, globalConfig);
        // 数据源配置
        getDataSource(generator);
        // 策略配置
        getStrategyConfig(generator);
        // 自定义配置
        getInjectionConfig(generator);
        // 模板配置 不要带上.ftl/.vm
        getTemplateConfig(generator);
        // 包名配置
        getPackageConfig(generator);
        // 执行生成
        generator.execute();

    }

    /**
     * 全局配置
     */
    private static GlobalConfig getGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        // 输出文件路径
        globalConfig.setOutputDir(path);
        // 是否覆盖已有文件
        globalConfig.setFileOverride(false);
        // 是否打开输出目录
        globalConfig.setOpen(false);
        // XML 二级缓存
        globalConfig.setEnableCache(false);
        // 作者
        globalConfig.setAuthor(FileUtils.readYml("gencode.author"));
        // 开启 swagger2 模式
        globalConfig.setSwagger2(false);
        // XML ResultMap
        globalConfig.setBaseResultMap(true);
        // XML columList
        globalConfig.setBaseColumnList(true);
        globalConfig.setFileOverride(true);

        return globalConfig;

    }

    /**
     * 自定义文件命名，注意 %s 会自动填充表实体属性！
     *
     * @param globalConfig
     */
    private static void getFileName(AutoGenerator generator, GlobalConfig globalConfig) {
        globalConfig.setControllerName("%sController");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setEntityName("%s");
        generator.setGlobalConfig(globalConfig);
    }

    /**
     * 数据源配置
     *
     * @param generator
     */
    private static void getDataSource(AutoGenerator generator) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(FileUtils.readYml("datasource.username"));
        dsc.setPassword(FileUtils.readYml("datasource.password"));
        dsc.setUrl(FileUtils.readYml("datasource.url"));
        generator.setDataSource(dsc);
    }

    /**
     * 策略配置
     *
     * @param generator
     */
    private static void getStrategyConfig(AutoGenerator generator) {
        StrategyConfig strategy = new StrategyConfig();
        // 是否大写命名
        // strategy.isCapitalMode();
        // 是否跳过视图
        // strategy.isSkipView();
        // 乐观锁字段
        strategy.setVersionFieldName("version");
        // 逻辑删除
        //strategy.setLogicDeleteFieldName("del_Flag");
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 表字段生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 表前缀
        //strategy.setTablePrefix(new String[] { "so_" });
        // 字段前缀
        strategy.setFieldPrefix(new String[]{"so_"});
        // 自定义继承的 Entity 类全称，带包名
        strategy.setSuperEntityClass(FileUtils.readYml("system.superEntityClass"));
        String superEntityColumns = FileUtils.readYml("system.uperEntityColumns");
        // 自定义基础的 Entity 类，公共字段
        strategy.setSuperEntityColumns(superEntityColumns.split(","));
        // 自定义继承的 Mapper 类全称，带包名
        strategy.setSuperMapperClass("us.epayworld.common.baseconfig.mybatisplus.core.BaseCURDMapper");
        // 自定义继承的 Service 类全称，带包名
        strategy.setSuperServiceClass("us.epayworld.common.baseconfig.mybatisplus.core.IBaseCURD");
        // 自定义继承的 ServiceImpl 类全称，带包名
        strategy.setSuperServiceImplClass("us.epayworld.common.baseconfig.mybatisplus.core.BaseCURDImpl");
        // 自定义继承的 Controller 类全称，带包名
        strategy.setSuperControllerClass(null);
        // 需要生成的表
        String talbe = FileUtils.readYml("gencode.inTables");
        strategy.setInclude(talbe.split(","));

        generator.setStrategy(strategy);
    }

    /**
     * 自定义配置
     *
     * @param mpg
     */
    private static void getInjectionConfig(AutoGenerator mpg) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        // 自定义entityBo的代码模板
        String boTemplatePath = "/templates/bo.java.vm";
        String voTemplatePath = "/templates/vo.java.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        //自定义配置会被优先输出
        focList.add(
                new FileOutConfig(boTemplatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return path + "/" + packageName.replace(".", "/") + "/entity/bo/" + tableInfo.getEntityName() + "BO.java";
                    }
                });
        focList.add(
                new FileOutConfig(voTemplatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return path + "/" + packageName.replace(".", "/") + "/entity/vo/" + tableInfo.getEntityName() + "VO.java";
                    }
                });
        cfg.setFileOutConfigList(focList);


        mpg.setCfg(cfg);
    }

    /**
     * 模板配置 不要带上.ftl/.vm
     *
     * @param generator
     */
    private static void getTemplateConfig(AutoGenerator generator) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController("templates/controller.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setXml("templates/mapper.xml");
        templateConfig.setEntity("templates/entity.java");
        generator.setTemplate(templateConfig);
    }


    /**
     * 包名配置
     *
     * @param generator
     */
    private static void getPackageConfig(AutoGenerator generator) {
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        pc.setXml("xml");
        pc.setEntity("entity");

        generator.setPackageInfo(pc);
    }


}
