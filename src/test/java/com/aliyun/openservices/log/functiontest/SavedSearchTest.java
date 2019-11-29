package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.SavedSearch;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateSavedSearchRequest;
import com.aliyun.openservices.log.request.DeleteSavedSearchRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SavedSearchTest extends FunctionTest {

    private static final String TEST_PROJECT = "project-to-test-savedsearch";

    @Before
    public void setUp() {
        safeCreateProject(TEST_PROJECT, "savedsearch test");
        waitForSeconds(5);
    }

    @Test
    public void testCreateSavedSearch() throws LogException {
        String savedsearchName = "savedsearchtest";
        for (int i = 0; i < 100; i++) {
            SavedSearch savedSearch = new SavedSearch();
            savedSearch.setSavedSearchName(savedsearchName + i);
            savedSearch.setLogstore("logstore-1");
            savedSearch.setDisplayName(savedsearchName + i);
            savedSearch.setSearchQuery("*");
            client.createSavedSearch(new CreateSavedSearchRequest(TEST_PROJECT, savedSearch));
        }
        SavedSearch savedSearch = new SavedSearch();
        savedSearch.setSavedSearchName(savedsearchName + "100");
        savedSearch.setLogstore("logstore-1");
        savedSearch.setDisplayName(savedsearchName);
        savedSearch.setSearchQuery("*");
        try {
            client.createSavedSearch(new CreateSavedSearchRequest(TEST_PROJECT, savedSearch));
        } catch (LogException ex) {
            assertEquals(ex.GetErrorMessage(), "savedsearch quota exceed");
            assertEquals(ex.GetErrorCode(), "ExceedQuota");
        }
        for (int i = 0; i < 100; i++) {
            client.deleteSavedSearch(new DeleteSavedSearchRequest(TEST_PROJECT, savedsearchName + i));
        }
        try {
            client.deleteSavedSearch(new DeleteSavedSearchRequest(TEST_PROJECT, savedsearchName + "100"));
        } catch (LogException ex) {
            assertEquals(ex.GetErrorMessage(), "specified savedsearch does not exist");
            assertEquals(ex.GetErrorCode(), "SavedSearchNotExist");
        }
    }

    @After
    public void tearDown() throws Exception {
        client.DeleteProject(TEST_PROJECT);
        System.out.println("Delete project ok");
    }
}
