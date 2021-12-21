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
        AutoGenerator mpg = new AutoGenerator();
        GlobalConfig gc = new GlobalConfig();

        // 全局配置
        getAllSetting(gc);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        getFileName(mpg, gc);
        // 数据源配置
        getDataSource(mpg);
        // 策略配置
        getPolicConfig(mpg);
        // 自定义配置
        getCustomConfig(mpg);
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        getPath(mpg);

        // 执行生成
        mpg.execute();
    }

    /**
     * 全局配置
     *
     * @param gc
     */
    private static void getAllSetting(GlobalConfig gc) {
        // 全局配置
        gc.setOutputDir(path); // 输出文件路径
        // 是否覆盖已有文件
        gc.setFileOverride(false);
        gc.setOpen(false);
        gc.setFileOverride(true);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        // 作者
        gc.setAuthor(FileUtils.readYml("gencode.author"));
    }

    /**
     * 自定义文件命名，注意 %s 会自动填充表实体属性！
     *
     * @param gc
     */
    private static void getFileName(AutoGenerator mpg, GlobalConfig gc) {
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);
    }

    /**
     * 数据源配置
     *
     * @param mpg
     */
    private static void getDataSource(AutoGenerator mpg) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(FileUtils.readYml("datasource.username"));
        dsc.setPassword(FileUtils.readYml("datasource.password"));
        dsc.setUrl(FileUtils.readYml("datasource.url"));
        mpg.setDataSource(dsc);
    }

    /**
     * 策略配置
     *
     * @param mpg
     */
    private static void getPolicConfig(AutoGenerator mpg) {
        StrategyConfig strategy = new StrategyConfig();
        //     strategy.setTablePrefix(new String[] { "so_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel); // 表名生成策略
        String talbe = FileUtils.readYml("gencode.inTables");
        strategy.setInclude(talbe.split(",")); // 需要生成的表
        // 自定义实体父类
        strategy.setSuperEntityClass(FileUtils.readYml("system.superEntityClass"));
        String uperEntityColumns = FileUtils.readYml("system.uperEntityColumns");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(uperEntityColumns.split(","));
        // 自定义 mapper 父类
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");

        strategy.setSuperControllerClass(null);

        mpg.setStrategy(strategy);
    }

    /**
     * 自定义配置
     *
     * @param mpg
     */
    private static void getCustomConfig(AutoGenerator mpg) {
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
     * 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
     *
     * @param mpg
     */
    private static void getPath(AutoGenerator mpg) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        // 包配置/**/mpg.setTemplate(templateConfig);
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);
        pc.setEntity("entity");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        pc.setXml("xml");

        mpg.setPackageInfo(pc);
    }


}
