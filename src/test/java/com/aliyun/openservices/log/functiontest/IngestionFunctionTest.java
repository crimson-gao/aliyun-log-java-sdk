package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.AliyunOSSSource;
import com.aliyun.openservices.log.common.DelimitedTextFormat;
import com.aliyun.openservices.log.common.Ingestion;
import com.aliyun.openservices.log.common.IngestionConfiguration;
import com.aliyun.openservices.log.common.JobSchedule;
import com.aliyun.openservices.log.common.JobScheduleType;
import com.aliyun.openservices.log.common.JobState;
import com.aliyun.openservices.log.request.CreateIngestionRequest;
import org.junit.Test;

import java.util.Arrays;

public class IngestionFunctionTest extends JobIntgTest {

    private static String getIngestionName() {
        return "ingestion-" + getNowTimestamp();
    }

    private Ingestion createIngestion() {
        Ingestion ingestion = new Ingestion();
        String jobName = getIngestionName();
        ingestion.setName(jobName);
        ingestion.setState(JobState.ENABLED);
        ingestion.setDisplayName("OSS-test");
        IngestionConfiguration configuration = new IngestionConfiguration();
        configuration.setLogstore("test-logstore2");
        configuration.setRoleARN("acs:ram::1654218965343050:role/osstologservicerole");

        AliyunOSSSource source = new AliyunOSSSource();
        source.setBucket("yunlei-bill");
        source.setEncoding("UTF-8");
        source.setEndpoint("oss-cn-beijing.aliyuncs.com");
        source.setRoleARN("acs:ram::1654218965343050:role/osstologservicerole");

        DelimitedTextFormat format = new DelimitedTextFormat();
        //         format.put("type", "DelimitedText");
        //        format.put("fieldNames", "账期,财务单元,账号ID,账号,Owner账号,产品Code,产品,产品明细Code,产品明细,消费类型,消费时间,账单开始时间,账单结束时间,服务时长,订单号/账单号,账单类型,计费方式,实例ID,实例昵称,资源组,实例标签,实例配置,实例规格,公网IP,私网IP,地域,可用区,计费项,单价,单价单位,用量,用量单位,资源包抵扣,原价,优惠金额,优惠券抵扣,应付金额,现金支付,代金券抵扣,储值卡支付金额,欠费金额");
        //        format.put("fieldDelimiter", ",");
        //        format.put("quoteChar", "\"");
        //        format.put("escapeChar", "\\");
        //        format.put("skipLeadingRows", "1");
        format.setEscapeChar("\\");
        format.setSkipLeadingRows(1);
        format.setFieldNames(Arrays.asList("账期,财务单元,账号ID,账号,Owner账号,产品Code,产品,产品明细Code,产品明细,消费类型,消费时间,账单开始时间,账单结束时间,服务时长,订单号/账单号,账单类型,计费方式,实例ID,实例昵称,资源组,实例标签,实例配置,实例规格,公网IP,私网IP,地域,可用区,计费项,单价,单价单位,用量,用量单位,资源包抵扣,原价,优惠金额,优惠券抵扣,应付金额,现金支付,代金券抵扣,储值卡支付金额,欠费金额".split(",")));
        format.setQuoteChar("\"");
        format.setFieldDelimiter(",");
        source.setFormat(format);
        configuration.setSource(source);
        ingestion.setConfiguration(configuration);
        //
//        Map<String, Object> format = new HashMap<>();
//        dataSourceConfig.put("pattern", "^1654218965343050\\_\\w+\\_20190621");
//        format.put("type", "DelimitedText");
//        format.put("fieldNames", "账期,财务单元,账号ID,账号,Owner账号,产品Code,产品,产品明细Code,产品明细,消费类型,消费时间,账单开始时间,账单结束时间,服务时长,订单号/账单号,账单类型,计费方式,实例ID,实例昵称,资源组,实例标签,实例配置,实例规格,公网IP,私网IP,地域,可用区,计费项,单价,单价单位,用量,用量单位,资源包抵扣,原价,优惠金额,优惠券抵扣,应付金额,现金支付,代金券抵扣,储值卡支付金额,欠费金额");
//        format.put("fieldDelimiter", ",");
//        format.put("quoteChar", "\"");
//        format.put("escapeChar", "\\");
//        format.put("skipLeadingRows", "1");
//
//        Map<String, Object> source = new HashMap<>();
//        source.put(OSSConfig.ENDPOINT, "oss-cn-beijing.aliyuncs.com");
//        source.put(OSSConfig.TEMP_FILE_DIR, "/tmp/data");
//        source.put(OSSConfig.BUCKET_NAME, "yunlei-bill");
//        source.put("format", format);
//        source.put("roleARN", "acs:ram::1654218965343050:role/osstologservicerole");
////        source.put("type", "AliyunOSS");
//
//        Map<String, Object> userConfig = new HashMap<>();
//        userConfig.put("logstore", "test-logstore2");
//        userConfig.put("roleARN", "acs:ram::1654218965343050:role/osstologservicerole");
//        userConfig.put("source", source);
//        args.put("userConfig", userConfig);
        JobSchedule schedule = new JobSchedule();
        schedule.setInterval("5m");
        schedule.setType(JobScheduleType.FIXED_RATE);
        ingestion.setSchedule(schedule);
        return ingestion;
    }

    @Test
    public void testCrud() throws Exception {
        Ingestion ingestion = createIngestion();
        client.createIngestion(new CreateIngestionRequest("ali-sls-etl-staging", ingestion));
    }

}
