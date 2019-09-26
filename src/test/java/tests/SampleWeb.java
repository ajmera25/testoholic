package tests;

import org.testng.annotations.Test;

import core.BaseTest;
import pageobjects.desktop.SamplePageObject;

public class SampleWeb extends BaseTest{

    @Test
    public void stepInFB() throws Exception{
        SamplePageObject obj = new SamplePageObject(driver);
        obj.search("step-in forum facebook");
        obj.navigateToPosts();
        obj.navigateToPhotos();
    }
	
}
