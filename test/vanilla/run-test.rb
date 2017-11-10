base_dir = File.expand_path(File.join(File.dirname(__FILE__), "..", ".."))
lib_dir = File.join(base_dir, "lib")
test_dir = File.join(base_dir, "test")

$LOAD_PATH.unshift(lib_dir)
$LOAD_PATH.unshift(test_dir)

# This `require` makes gems embedded in embulk-core.jar available to be required.
require "#{base_dir}/classpath/embulk-core-0.8.36.jar"

# This Gem.path trick is needed to load gems embedded in embulk-core.jar when run through jruby-gradle-plugin.
# It can be after `require "embulk-core.jar"`.
Gem.path << 'uri:classloader://'
Gem::Specification.reset

require "test/unit"

Dir.glob("#{base_dir}/test/vanilla/**/test{_,-}*.rb") do |file|
  require file.sub(/\.rb$/,"")
end

exit Test::Unit::AutoRunner.run
