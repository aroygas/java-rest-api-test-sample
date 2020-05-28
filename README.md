# java-rest-api-test-sample

This is a sample Java based project for API tests of Todoist Create new task method. 

It uses REST Assured, Cucumber, JUnit, Allure and Gradle.


---

# Set up #

1. Install Git
2. Install Java 11
3. Install Gradle
4. Install Allure (optionally) to get fancy reports

## Clone this repository

You can clone this repo using git console via https:
```
git clone https://github.com/aroygas/java-rest-api-test-sample.git
```

---

# Running tests #

Test are ran using gradle "test" task.<br>
To run all the tests just call gradle with "clean test" command:
```
gradle clean test
```

## Tags ##

Each scenario has some set of tags. <br>
Test suite for a particular run is managed by mentioned tags. <br>
There are a lot of tags and they follow the rules: <br>
`@smoke`                                 - run only tests tagged as @smoke <br>
`not @regression`                     - exclude tests tagged as @regression from run <br>
`@negative or @regression`    - run tests tagged as @negative OR tagged as @regression <br>
`@smoke and @negative` - run tests tagged as @smoke AND tagged as @negative <br>
`@smoke and not @negative`  - run tests tagged as @smoke AND exclude tests tagged as @negative from the run <br>

So, to run only smoke tests run:
```
gradle clean test -Dtags="@smoke"
```

---

# Generating html report #

To generate a report using Allure run:
```
allure serve ./build/allure-results
```

---

# Test scenarios Create a new task

Same cases can be found in resources\features\Test.feature <br>
It is suggested to read them in some cucumber highlighting tool like IntelliJ IDEA with Cucumber plugin.

```
Feature: Creates a new task on Todoist
  As a user
  In order to create a new task
  I need to send POST request using Todoist API

  @smoke
  Scenario: Create a simple task with single mandatory content field
    When I create a task named "Simple task"
    Then I should see a 200 status code in response

  @regression
  Scenario: Create a complex task with all fields set
    When I create a task with data:
    """
    {"content": "Complex task",
    "project_id": 2237019745,
    "section_id": 0,
    "parent": 3929864419,
    "order": 1,
    "label_ids": [],
    "priority": 2,
    "due_lang": "en",
    "due_date": "2020-09-27"
    }
    """
    Then I should see a 200 status code in response

  @regression
  Scenario: Create a task with due_string
    When I create a task with data:
    """
    {"content": "Due string",
    "priority": 2,
    "due_lang": "en",
    "due_string": "next Monday"
    }
    """
    Then I should see a 200 status code in response

  @regression
  Scenario: Create a task with due_datetime
    When I create a task with data:
    """
    {"content": "Due datetime",
    "priority": 2,
    "due_lang": "en",
    "due_datetime": "2021-05-04T01:23:45.01+07:00"
    }
    """
    Then I should see a 200 status code in response


  #Wrong section number is ignored and section_id=0 is given instead.
  #Such behaviour should be discussed with developers.
  #This one might be a bug!
  @negative  @regression
  Scenario: Send invalid section id
    When I create a task with data:
    """
    {"content": "Invalid section id",
    "section_id": 123
    }
    """
    Then I should see a 200 status code in response

  @negative  @regression
  Scenario: Send empty request
    When I create a task with data:
    """
    """
    Then I should see a 400 status code in response

  @negative  @regression
  Scenario: Send some parameters without mandatory one
    When I create a task with data:
    """
    {
    "priority": 2,
    "due_lang": "en",
    "due_datetime": "2021-05-04T01:23:45.01+07:00"
    }
    """
    Then I should see a 400 status code in response

  @negative  @regression
  Scenario: Send due_string and due_date at the same time
    When I create a task with data:
    """
    {"content": "Due string and date",
    "priority": 2,
    "due_lang": "en",
    "due_date": "2022-01-23",
    "due_string": "Tomorrow"
    }
    """
    Then I should see a 400 status code in response

  @negative  @regression
  Scenario: Send invalid priority id
    When I create a task with data:
    """
    {"content": "Invalid priority",
    "priority": 5
    }
    """
    Then I should see a 400 status code in response

  #Checking JSON type of parameters validation actually
  @negative  @regression
  Scenario: Send invalid type for order parameter
    When I create a task with data:
    """
    {"content": "Invalid order",
    "order": "string"
    }
    """
    Then I should see a 400 status code in response

  @negative  @regression
  Scenario: Send invalid due_string
    When I create a task with data:
    """
    {"content": "Invalid due date string",
    "due_string": "not a date string"
    }
    """
    Then I should see a 400 status code in response

  @negative  @regression
  Scenario: Send due_date with wrong day number
    When I create a task with data:
    """
    {"content": "Due date in far future",
    "due_date": "2022-05-55"
    }
    """
    Then I should see a 400 status code in response

  ############################################################################
  # It looks like a small bug with following cases - the situation with invalid Ids is not handled by backend.
  # Internal Server Error with 500 status code is shown instead of some meaningful message explaining that there is no such data.
  #############################################################################
  @negative  @regression
  Scenario: Send invalid project id
    When I create a task with data:
    """
    {"content": "Invalid project id",
    "project_id": 1
    }
    """
    Then I should see a 500 status code in response

  @negative  @regression
  Scenario: Send invalid parent id
    When I create a task with data:
    """
    {"content": "Invalid parent id",
    "parent": 1
    }
    """
    Then I should see a 500 status code in response

  @negative  @regression
  Scenario: Send due_date in far past
    When I create a task with data:
    """
    {"content": "Due date in far past",
    "due_date": "1600-01-23"
    }
    """
    Then I should see a 500 status code in response

  @negative  @regression
  Scenario: Send due_date in last day of the date type
    When I create a task with data:
    """
    {"content": "Due date in far future",
    "due_date": "9999-12-31"
    }
    """
    Then I should see a 500 status code in response


```