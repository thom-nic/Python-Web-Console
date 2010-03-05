
# Download YUI files - they are hosted from YUI's servers when in production 
# but hosted locally when in debug (development mode)
 
yui_version=2.8.0r4
dest_base=src/main/webapp/static/hosted/yui 

mkdir -p $dest_base
cd $dest_base
wget http://yui.yahooapis.com/${yui_version}/build/reset-fonts-grids/reset-fonts-grids.css
wget http://yui.yahooapis.com/${yui_version}/build/base/base-min.css

file="yui_$yui_version.zip"

wget "http://yuilibrary.com/downloads/yui2/$file"
unzip $file "yui/build/*"

# Ojay files must be included since there is no hosted version.  We use the 
# minified versions when in production (i.e. debug is off)
ojay_version=0.4.1
file=ojay-$version.zip
wget http://ojay.googlecode.com/files/$file

cd ../../js/
mkdir ojay
cd ojay
wget http://ojay.googlecode.com/files/$file
unzip $file "ojay-$version/ojay/*"