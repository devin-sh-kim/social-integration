package net.ujacha.demo.social.vo;

import java.util.HashMap;

public class RepositoryParam extends HashMap<String, Object> {

    public static RepositoryParamBuilder builder() {
        return new RepositoryParamBuilder();
    }

    public static class RepositoryParamBuilder {
        private RepositoryParam param;

        public RepositoryParamBuilder() {
            this.param = new RepositoryParam();
        }

        public RepositoryParamBuilder add(String key, Object value){
            this.param.put(key, value);
            return this;
        }

        public RepositoryParam build(){
            return this.param;
        }

    }

}
