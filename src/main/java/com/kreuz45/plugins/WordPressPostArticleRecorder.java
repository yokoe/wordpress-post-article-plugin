package com.kreuz45.plugins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import com.kreuz45.plugins.WordPressArticle;

public class WordPressPostArticleRecorder extends Recorder {
	
	private final String url, user, password, title, body, category, basic_user_name, basic_password;
	private Boolean publish;

	@DataBoundConstructor
	public WordPressPostArticleRecorder(String url, String user,
			String password, String title, String body, String category,
			String basic_user_name, String basic_password, Boolean publish) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.title = title;
		this.body = body;
		this.category = category;
		this.basic_user_name = basic_user_name;
		this.basic_password = basic_password;
		this.publish = publish;
	}
	
	/**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getUrl() {
        return url;
    }
    public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}
	
	public Boolean getPublish() {
		return publish;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getBasic_user_name() {
		return basic_user_name;
	}

	public String getBasic_password() {
		return basic_password;
	}

	@Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
		WordPressArticle article = new WordPressArticle(url, user, password, title, body, category, basic_user_name, basic_password, publish);
        try {
			return article.post(build.getEnvironment(listener));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }

	@Override
    public DescriptorImpl getDescriptor() {
        return DESCRIPTOR;
    }
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
	@Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher>
    {
        public DescriptorImpl() {
            super(WordPressPostArticleRecorder.class);
        }
                
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            save();
            return true;
        }
                
        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Post an article to WordPress";
        }
    }
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

}
