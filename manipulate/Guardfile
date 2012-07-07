guard 'sass', :input => 'src/sass', :output => 'public'
guard 'haml', :input => 'src/haml', :output => 'public' do
  watch %r{^src/haml/.+\.haml$}
end

guard 'livereload', :apply_js_live => false do
  watch(%r{public/.+\.(css|js|html)})
end
