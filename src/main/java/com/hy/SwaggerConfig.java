package com.hy;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
        //扫描指定包中的swagger注解 //.apis(RequestHandlerSelectors.basePackage("com.xia.controller")) //扫描所有有注解的api，用这种方式更灵活
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any())
        .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("app 接口")
                .description("1.登陆 2.我的金币 3.我的道具。")
                .termsOfServiceUrl("http://localhost:8083")
                .contact("Brian.wang")
                .version("1.0.0")
                .build();
    }








/*//    private String pathMapping;

    private ApiInfo initApiInfo(){

        ApiInfo apiInfo = new ApiInfo("数据存储项目 Platform API", // 大标题
                initContextInfo(), // 简单的描述
                "1.0.0", // 版本
                "服务条款", "后台开发团队", // 作者
                "The Apache License, Version 2.0", // 链接显示文字
                "http://www.baidu.com"// 网站链接
        );
        return apiInfo;
    }

    private String initContextInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("REST API 设计在细节上有很多自己独特的需要注意的技巧，并且对开发人员在构架设计能力上比传统 API 有着更高的要求。").append("<br/>")
                .append("以下是本项目的API文档");
        return sb.toString();
    }

    @Bean
    public Docket restfulApi() {
        System.out.println("http://localhost:8080" + pathMapping + "/swagger-ui.html");
        return new Docket(DocumentationType.SWAGGER_2).groupName("RestfulApi")
                // .genericModelSubstitutes(DeferredResult.class)
                .genericModelSubstitutes(ResponseEntity.class).useDefaultResponseMessages(true).forCodeGeneration(false)
                .pathMapping(pathMapping) // base，最终调用接口后会和paths拼接在一起
                .select().paths(doFilteringRules()).build().apiInfo(initApiInfo());
        // .select().paths(Predicates.not(PathSelectors.regex("/error.*"))).build().apiInfo(initApiInfo());

    }

    *//**
     * 设置过滤规则 这里的过滤规则支持正则匹配
     //若有静态方法在此之前加载就会报集合相关的错误.
     *
     * @return
     *//*
    private Predicate<String> doFilteringRules() {
//		return Predicates.not(PathSelectors.regex("/error.*"));
//		return or(regex("/hello.*"), regex("/rest/adxSspFinanceManagement.*"));//success
        return or(regex("/hello.*"), regex("/rest.*"));
    }*/
}
