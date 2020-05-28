Feature: Creates a new task on Todoist
  As a user
  In order to create a new task
  I need to send POST request using Todoist API

  @smoke
  Scenario: Create a simple task with single mandatory content field
    When I create a task named "Test 111"
    Then I should see a 200 status code in response

  @regression
  Scenario: Create a full task with all fields set
    When I create a task with data:
    """
    {"content": "654",
    "project_id": 2237019745,
    "section_id": 0,
    "parent": 3929864419,
    "order": 1,
    "label_ids": [],
    "priority": 2,
    "due_lang": "en",
    "due": {
		"recurring": false,
		"string": "today",
		"date": "2020-09-27"
	  }
    }
    """
    Then I should see a 200 status code in response

  @negative
  Scenario: Send empty request
    When I create a task with data:
    """
    """
    Then I should see a 400 status code in response