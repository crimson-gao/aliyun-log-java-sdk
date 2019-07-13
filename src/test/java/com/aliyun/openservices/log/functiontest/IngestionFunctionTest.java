package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.AliyunOSSSource;
import com.aliyun.openservices.log.common.DelimitedTextFormat;
import com.aliyun.openservices.log.common.Ingestion;
import com.aliyun.openservices.log.common.IngestionConfiguration;
import com.aliyun.openservices.log.common.JobSchedule;
import com.aliyun.openservices.log.common.JobScheduleType;
import com.aliyun.openservices.log.common.JobState;
import com.aliyun.openservices.log.request.CreateIngestionRequest;
import com.aliyun.openservices.log.request.DeleteIngestionRequest;
import com.aliyun.openservices.log.request.DisableJobRequest;
import com.aliyun.openservices.log.request.GetIngestionRequest;
import com.aliyun.openservices.log.request.ListIngestionRequest;
import com.aliyun.openservices.log.request.StartIngestionRequest;
import com.aliyun.openservices.log.request.StopIngestionRequest;
import com.aliyun.openservices.log.request.UpdateIngestionRequest;
import com.aliyun.openservices.log.response.GetIngestionResponse;
import com.aliyun.openservices.log.response.ListIngestionResponse;
import org.junit.Test;

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
//        configuration.setRoleARN("acs:ram::1654218965343050:role/osstologservicerole");

        AliyunOSSSource source = new AliyunOSSSource();
        source.setBucket("yunlei-bill");
        source.setEncoding("UTF-8");
        source.setEndpoint("oss-cn-beijing.aliyuncs.com");
        source.setRoleARN("acs:ram::1654218965343050:role/osstologservicerole");

        DelimitedTextFormat format = new DelimitedTextFormat();
        format.setEscapeChar("\\");
//        format.setFieldNames(Arrays.asList("账期,财务单元,账号ID,账号,Owner账号,产品Code,产品,产品明细Code,产品明细,消费类型,消费时间,账单开始时间,账单结束时间,服务时长,订单号/账单号,账单类型,计费方式,实例ID,实例昵称,资源组,实例标签,实例配置,实例规格,公网IP,私网IP,地域,可用区,计费项,单价,单价单位,用量,用量单位,资源包抵扣,原价,优惠金额,优惠券抵扣,应付金额,现金支付,代金券抵扣,储值卡支付金额,欠费金额".split(",")));
        format.setFirstRowAsHeader(true);
        format.setSkipLeadingRows(0);
        format.setQuoteChar("\"");
        format.setFieldDelimiter(",");
        source.setFormat(format);
        configuration.setSource(source);
        ingestion.setConfiguration(configuration);
        JobSchedule schedule = new JobSchedule();
        schedule.setInterval("1h");
        schedule.setType(JobScheduleType.FIXED_RATE);
        ingestion.setSchedule(schedule);
        return ingestion;
    }

    @Test
    public void testCrud() throws Exception {
        Ingestion ingestion = createIngestion();
        System.out.println(ingestion.getName());
        client.createIngestion(new CreateIngestionRequest("ali-sls-etl-staging", ingestion));
    }

    @Test
    public void testGet() throws Exception {
        GetIngestionResponse response = client.getIngestion(new GetIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
        Ingestion ingestion = response.getIngestion();
        System.out.println(ingestion.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        GetIngestionResponse response = client.getIngestion(new GetIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
        Ingestion ingestion = response.getIngestion();
        AliyunOSSSource source = (AliyunOSSSource) ingestion.getConfiguration().getSource();
        source.setPattern("1654218965343050_InstanceDetail_\\d{8}");
        client.updateIngestion(new UpdateIngestionRequest("ali-sls-etl-staging", ingestion));
    }

    @Test
    public void testInvalidOperation() throws Exception {
//        client.enableJob(new EnableJobRequest("ali-sls-etl-staging", "ingestion-1562994887"));
        client.disableJob(new DisableJobRequest("ali-sls-etl-staging", "ingestion-1562994887"));
    }

    @Test
    public void testDelete() throws Exception {
        client.getIngestion(new GetIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
     //   client.stopIngestion(new StopIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
        client.stopIngestion(new StopIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
//        client.startIngestion(new StartIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
//        client.startIngestion(new StartIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
        client.startIngestion(new StartIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
        client.deleteIngestion(new DeleteIngestionRequest("ali-sls-etl-staging", "ingestion-1562994571"));
    }

    @Test
    public void testList() throws Exception {
        ListIngestionResponse response = client.listIngestion(new ListIngestionRequest("ali-sls-etl-staging"));
        for (Ingestion ingestion : response.getResults()) {
            System.out.println(ingestion.getName());
        }
    }
}
