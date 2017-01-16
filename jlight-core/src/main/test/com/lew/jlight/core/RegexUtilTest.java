package com.lew.jlight.core;

import com.lew.jlight.core.util.RegexUtil;

import org.junit.Assert;
import org.junit.Test;

public class RegexUtilTest {

    @Test
    public void testEmail() throws Exception {
        boolean isEmail = RegexUtil.isEmail("751185330@qq.com");
        Assert.assertTrue(isEmail);
        isEmail = RegexUtil.isEmail("7511853sfdsdf");
        Assert.assertFalse(isEmail);
    }
    @Test
    public void testMobile() throws Exception {
        boolean isMobile = RegexUtil.isMobile("751185330@qq.com");
        Assert.assertFalse(isMobile);
        isMobile = RegexUtil.isMobile("13428281893");
        Assert.assertTrue(isMobile);
    }
}
