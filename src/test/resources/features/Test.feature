Feature: Create a new task on Todoist
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


  @edge  @regression
  Scenario: Send very long content string
    When I create a task with data:
    """
    {"content": "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123457012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234"
    }
    """
    Then I should see a 200 status code in response

  @edge  @regression
  Scenario: Send content string with special characters
    When I create a task with data:
    """
    {"content": "'`@#$%^&*()_+><:"}{?:"
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

  #Not an actual test just a technical scenario that can be either called or avoided in the real run by Jenkins for example
  @clean
  Scenario: Delete all created tasks
   When I clean up test tasks