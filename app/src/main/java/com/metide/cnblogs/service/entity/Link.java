package com.metide.cnblogs.service.entity;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Author   metide
 * Date     2017/5/7
 */
@Root(name = "link")
public class Link {
    @Attribute
    public String rel;
    @Attribute
    public String href;
}
