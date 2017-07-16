Arya Alucon
===================
[![](https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=in.surana.ashish.alucon&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1)

Description
-------------
A Native Android Application made for the College, dedicated to the Alumni, where they can see their friends and get to know about the recent college activities




----------

Requirements
-------------

> **Tip:** Check out the [<i class="fa fa-github"></i> alumni-app-server](https://github.com/ashishsurana/alumni-app-server) (github repo) for the node js server for this android application

#### Software used :
	jdk - open jdk
	Java Version - 1.8

#### Android 
	compileSdkVersion - 24
	minSdkVersion : 15
	targetSdkVersion : 24
	buildToolsVersion '23.0.3'
 For more dependencies - refer to the build.gradle file

#### Crashytitics 
	Fabric.io by twitter



-------------------



Installation
-------------
----------
clone the project by 
> git clone https://github.com/ashishsurana/alumni 
	
Import the project into android studio
> File > New > Import Project
> and pick the project , where it is cloned 

Sync all the gradle files
> Tools > Android > Sync Project with gradle files

----------


Usage / Changes to be done
--------------------
Event Icon Links ( that appear in the events list)
X is numbers from 1-9
> Replace the links in strings.xml by your link 
> `<string name="event_X_image_url"> http://your_image_link </string>`



Base Url
Change your backend server base url in
> in/surana/ashish/alucon/network/ApiClient.java class 
public static final String BASE_URL

Reconfigure the fabric for your project
> Remove the API keys from manifest

Changing the Frequently Asked Questions
> strings.xml
name for questiosn :  ques_X
 name for answers : abswerX

Other important changes in strings.xml
> - developed_by
> - github_link
> - url_facebook_page
> - url_college
> - app_share_text
> - Arrays
>  - year_array
>  - branch_array
