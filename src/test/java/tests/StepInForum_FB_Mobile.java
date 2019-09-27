package tests;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import core.APIHelper;
import core.BaseTest;
import pageobjects.mobile.FacebookPage;
import pageobjects.mobile.GooglePage;
import utilities.JsonTemplate;

public class StepInForum_FB_Mobile extends BaseTest{
    
	public StepInForum_FB_Mobile() {
		super("mobileWeb");
	}

	GooglePage google = null;
	FacebookPage facebook = null;
	HashMap<String, Integer> albumNames;
    
    @Test(description = "Search google for facebook post and click on it")
    public void test001_StepInGoogle() throws Exception{
      	google = new GooglePage(appiumDriver);
    	facebook = new FacebookPage(appiumDriver);
    	Assert.assertTrue(google.searchOnGoogle(), "Unable to search in Google");
        Assert.assertTrue(google.clickFbPost(), "Unable to click on FB Post");
    }
    
    @Test(dependsOnMethods = "test001_StepInGoogle", description = "Open the post with more than 4 photos")
    public void test002_StepInFB() throws Exception{    
        Assert.assertTrue(facebook.openFbPost(), "Unable to Open FB Post");
        Assert.assertTrue(facebook.openPhotosPost(), "Unable to Open Photos");
    }
    
    @Test(dependsOnMethods = "test002_StepInFB", description = "Download the photos and validate its size")
    public void test003_DownloadPhotosAndCheckSize() throws Exception{
    	SoftAssert ImageSizeVerification = new SoftAssert();
    	facebook.clickImage();
        int imageCounter=1;
        utilities.FileUtils fileUtils = new utilities.FileUtils();
        while(imageCounter<=5) {
            String filePath = System.getProperty("user.dir") + "/target/MobilePhotos/" + imageCounter + ".jpg";
            int size = ( fileUtils).getFileSizeInKb(filePath);
            ImageSizeVerification.assertTrue(size > 0, "Image Size is not greater than O KB for Image: " +  imageCounter);
            imageCounter++;
        }
        ImageSizeVerification.assertAll();
    }
    
    @Test(dependsOnMethods = "test003_DownloadPhotosAndCheckSize", description = "Navigate to Photos and get all album names and its photo count")
    public void test004_getListOfAlbums(){
    	this.albumNames  = facebook.getListOfAlbumns();
    	Assert.assertFalse(albumNames.isEmpty(), "Failed to get album names");
    }
    
    @Test(dependsOnMethods = "test004_getListOfAlbums", description = "Create data json file, upload it and verify response" )
    public void test005_verifyFileUploaded(){
        String fileName = new utilities.FileUtils().createJSONFile(new JsonTemplate(teamName, albumNames).getJsonString());
        APIHelper apiHelper = new APIHelper();
        String response = apiHelper.upload(fileName);
        Assert.assertTrue(response.contains(teamName),"Team name is not present in the response => "+response);
    }
    
}


