package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.AuditJob;
import com.aliyun.openservices.log.common.AuditJobConfiguration;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.DeleteAuditJobResponse;
import com.aliyun.openservices.log.response.GetAuditJobResponse;
import org.junit.Test;

public class AuditJobFunctionTest extends FunctionTest {

    String project = "project-to-test-alert";
    String jobName = "test_audit_job";

    @Test
    public void testCreate() throws Exception {
        AuditJob auditJob = new AuditJob();
        auditJob.setName(jobName);
        auditJob.setDisplayName("test audit job");
        auditJob.setDescription("...");
        auditJob.setStatus("xxx");
        AuditJobConfiguration configuration = new AuditJobConfiguration();
        configuration.setDetail("{}");
        auditJob.setConfiguration(configuration);
        client.createAuditJob(new CreateAuditJobRequest(project, auditJob));
    }

    @Test
    public void testGet() throws Exception {
        GetAuditJobResponse response = client.getAuditJob(new GetAuditJobRequest(project, jobName));
        AuditJob ri = response.getAuditJob();
        System.out.println("audit_job: " + ri.getName() + "\nstatus: " +  ri.getStatus());
    }

    @Test
    public void testDelete() throws Exception {
        DeleteAuditJobResponse response = client.deleteAuditJob(new DeleteAuditJobRequest(project, jobName));
        System.out.println(response.GetAllHeaders());
    }
}
