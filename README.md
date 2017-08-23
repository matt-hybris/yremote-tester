# yremote-tester

Simple utility project that runs tests on a running hybris server. 

Written entirely in Groovy. 

## Why?

I'm a strong supporter of TDD and unit testing in general. Even if you're not doing pure TDD, 
(which I don't do all the time), it's still important to have good test coverage. 

Running integration tests on hybris can be a pain and very time consuming. Mostly because
you need to load up the hybris registry every time. So when your test fails and you make
a simple change, you have a pretty big turn around time until you can run the test again.

My suggestion to most is to use the hybris test servlet and have a good class reloader 
(such as JRebel, but there some other alternatives too).

To take it a bit further I created this library to abstract away the test servlet. The 
library on its' own is not very useful, but the goal is to create some IDE plugins
using this library. 

Currently I'm working on an IntelliJ plugin that will use this library. The plugin will
add a run option to run hybris tests on a running server and then display the results
directly in IntelliJ. 

I probably won't develop an Eclipse version on my own, but I would hope someone finds this
and can do the same for Eclipse.

Or who knows, maybe one day I'll be bored and make one for Eclipse too :) 

## Usage

This is the basic API:

<pre>
interface TestRunner {
    /**
     * Runs tests data on a default configured server.
     * @param testRunData Test suites and test cases to run
     * @return Test results and errors for failures
     */
    TestRunResult runTests(TestRunData testRunData)
}
</pre>

I will probably extend this to one that takes a server configuration as a parameter, although I'm not sure if it's 
necessary.
