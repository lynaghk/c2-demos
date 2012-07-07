#!/bin/bash
#Simple script that compiles static assets and rebuilds cljs

#abort on error
set -e

bundle install

rm -rf public

#run haml and sass via guard

ruby <<RUBY
require "bundler/setup"
require "guard"

Guard.setup
Guard::Dsl.evaluate_guardfile
Guard.guards.each do |guard|
  Guard::run_supervised_task(guard, :start)
  Guard::run_supervised_task(guard, :run_all)
end
RUBY

#rebuild cljs
lein cljsbuild clean && lein cljsbuild once

echo 'Hurray, build successful; run "lein run 8080" to launch server.'
