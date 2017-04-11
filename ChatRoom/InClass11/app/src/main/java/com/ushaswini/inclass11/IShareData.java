package com.ushaswini.inclass11;


/**
 * Vinnakota Venkata Ratna Ushaswini
 * IShareData
 * 10/04/2017
 */

public interface IShareData {

    public void postComment(MessageDetails messageDetails);

    //public void postCommentForImage(MessageDetails messageDetails);

    public void deleteImageMessage(MessageDetails messageDetails);
}
