package com.tao.hai.bean;

import lombok.Data;

import java.io.File;
import java.io.Serializable;
@Data
public class MailBean implements Serializable {
    /**发件人*/
    private String from;
    /**发件人名称*/
    private String fromName;
    /**收件人*/
    private String[]  toEmails;
    /**主题*/
    private String subject;
    /**邮件内容*/
    private Object data;
    /**模板*/
    private String template;
    /**邮件内容*/
    private String text;
    /**附件名称*/
    private String attachmentFilename;
    /**附件文件*/
    private File file;
    /**文件内容ID*/
    private String contentId;
}
