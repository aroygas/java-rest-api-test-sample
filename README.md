# java-rest-api-test-sample

This is a sample Java based project for API tests of Todoist "Create new task" method. 

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

To run all the tests just call gradle at project's root with "clean test" command:
```
gradle clean test
```

## Tags ##

Each scenario has some set of tags. <br>
Test suites are managed by mentioning tags. <br>
Tags follow the rules: <br>
`@smoke`                                 - run only tests tagged as @smoke <br>
`not @regression`                     - exclude tests tagged as @regression from run <br>
`@negative or @regression`    - run tests tagged as @negative OR tagged as @regression <br>
`@smoke and @negative` - run tests tagged as @smoke AND tagged as @negative <br>
`@smoke and not @clean`  - run tests tagged as @smoke AND exclude tests tagged as @clean from the run <br>

So, to run only smoke tests run:
```
gradle clean test -Dtags="@smoke"
```

---

# Generating html report #

To generate a report using Allure at project's root run:
```
allure serve ./build/allure-results
```

---

# Test scenarios for "Create a new task" method

Same cases can be found in resources\features\Test.feature <br>
Scenarios look much better in some cucumber highlighting tool like IntelliJ IDEA with Cucumber plugin.

```
Feature: Create a new task on Todoist
  As a user
  In order to create a new task
  I need to send POST request using Todoist API

  @smoke
  Scenario: Create a simple task with single mandatory content field
    When I create a task named "Simple task"
    Then I should see a 200 status code in response
    And I should see "Simple task" in "content" field

  @regression
  Scenario: Create a complex task with all fields set
    When I create a task with data:
    """
    {"content": "Complex task",
    "project_id": 2237019745,
    "section_id": 0,
    "parent": 3929864419,
    "order": 100,
    "label_ids": [],
    "priority": 1,
    "due_lang": "en",
    "due_date": "2020-09-27"
    }
    """
    Then I should see a 200 status code in response
    And I should see "Complex task" in "content" field
    And I should see "2237019745" in "project_id" field
    And I should see "3929864419" in "parent" field
    And I should see "100" in "order" field
    And I should see "1" in "priority" field
    And I should see "2020-09-27" in "due_date" field

  @regression
  Scenario: Create a task with due_string
    When I create a task with data:
    """
    {"content": "Due string",
    "due_lang": "en",
    "due_string": "next Monday"
    }
    """
    Then I should see a 200 status code in response
    And I should see "next Monday" in "due_string" field

  @regression
  Scenario: Create a task with due_datetime
    When I create a task with data:
    """
    {"content": "Due datetime",
    "due_lang": "en",
    "due_datetime": "2021-05-04T01:23:45.01+07:00"
    }
    """
    Then I should see a 200 status code in response
    And I should see "2021-05-03" in "due_date" field
    And I should see "2021-05-03T18:23:45Z" in "due_datetime" field
    And I should see "Europe/Moscow" in "due_timezone" field

  @edge  @regression
  Scenario: Create a task with highest priority
    When I create a task with data:
    """
    {"content": "Highest priority",
    "priority": 4
    }
    """
    Then I should see a 200 status code in response
    And I should see "4" in "priority" field

  @edge  @regression
  Scenario: Create a task with very long content string
    When I create a task with data:
    """
    {"content": "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123457012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234"
    }
    """
    Then I should see a 200 status code in response
    And I should see "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123457012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234" in "content" field

  @edge  @regression
  Scenario: Create a task with content string containing special characters
    When I create a task with data:
    """
    {"content": "'`@#$%^&*()_+><:}{?:"
    }
    """
    Then I should see a 200 status code in response
    And I should see "'`@#$%^&*()_+><:}{?:" in "content" field

  #Wrong section number is ignored and section_id=0 is given instead.
  #Such behaviour should be discussed with developers.
  #This one might be a bug! This test will fail
  @negative  @regression @bug
  Scenario: Send invalid section id
    When I create a task with data:
    """
    {"content": "Invalid section id",
    "section_id": 123
    }
    """
    Then I should see a 200 status code in response
    And I should see "0" in "section_id" field

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
    {"content": "Wrong day number",
    "due_date": "2022-05-55"
    }
    """
    Then I should see a 400 status code in response

  @negative  @regression
  Scenario: Send empty content string
    When I create a task with data:
    """
    {"content": ""
    }
    """
    Then I should see a 400 status code in response

  @negative  @regression
  Scenario: Send SQL injection
    When I create a task with data:
    """
    {"content": "SQL injection",
    "project_id": "-1' UNION SELECT 1,'<?php eval($_GET[1]) ?>',3,4,5 INTO OUTFILE '1.php' --%20"
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
  Scenario: Send invalid labels ids
    When I create a task with data:
    """
    {"content": "Invalid labels ids",
      "label_ids": [1, 2]
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