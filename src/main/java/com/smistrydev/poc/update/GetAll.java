/**
 * 
 */
package com.smistrydev.poc.update;

import io.searchbox.action.SingleResultAbstractDocumentTargetedAction;

public class GetAll extends SingleResultAbstractDocumentTargetedAction {

    protected GetAll(Builder builder) {
        super(builder);
        setURI(buildURI());
    }

    @Override
    public String getRestMethodName() {
        return "GET";
    }

    @Override
    public String getPathToResult() {
        return "_source";
    }

    public static class Builder extends SingleResultAbstractDocumentTargetedAction.Builder<GetAll, Builder> {

        /**
         * Index and ID parameters are mandatory but type is optional (_all will be used for type if left blank).
         * <br/><br/>
         * The get API allows for _type to be optional. Set it to _all in order to fetch the
         * first document matching the id across all types.
         */
        public Builder() {
//            this.index(index);
//            this.id(id);
            this.type("_all");
        }

        public GetAll build() {
            return new GetAll(this);
        }
    }
}