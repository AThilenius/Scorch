class ServiceController < ApplicationController
  $chat_messages = ['Hello', 'World', 'How', 'Are', 'You?']

  skip_before_filter :verify_authenticity_token, :only =>
                                                   [:get_minecraft_session,
                                                    :get_user_level_data,
                                                    :get_chat,
                                                    :push_chat]

  def get_minecraft_session

    # Get the user by GUID
    jsonRequest = JSON.parse(request.raw_post)
    if jsonRequest.nil? or jsonRequest['user_guid'].nil?
      render :json => { :error => 'Failed to parse JSON data' }
      return
    end

    user = User.where(:guid => jsonRequest['user_guid']).first
    if user.nil?
      render :json => { :error => "Failed to find a user with the GUID #{jsonRequest['user_guid']}" }
      return
    end

    render :json => { :username => "#{user.username}",
                      :uuid => "NA",
                      :access_token => "NA",
                      :user_properties => "{}",
                      :user_type => 'legacy'}
  end

  def get_user_level_data

    # Get the user by GUID
    jsonRequest = JSON.parse(request.raw_post)
    if jsonRequest.nil? or jsonRequest['authToken'].nil? or jsonRequest['levelNumber'].nil?
      render :json => { :error => 'Failed to parse JSON data' }
      return
    end

    data = User.join_all(
            'users.username as user_username,' +
            'users.firstName as user_first_name,' +
            'users.lastName as user_last_name,' +
            'users.permissions as user_permissions,' +
            'users.arenaLocation as user_arena_location,' +
            'assignment_descriptions.jarPath as assignment_jar_path,' +
            'assignment_descriptions.name as assignment_name,' +
            'assignment_descriptions.open_date as assignment_open_date,' +
            'assignment_descriptions.dueDate as assignment_due_date,' +
            'user_levels.id as user_level_id')
        .where("user_assignments.authToken = \"#{jsonRequest['authToken']}\"")
        .where("level_descriptions.levelNumber = \"#{jsonRequest['levelNumber']}\"")
        .first

    if data.nil?
      render :json => { :error => "Failed to find a user assignment with the GUID #{jsonRequest['authToken']}" }
      return
    end

    render :json => {
                :user_username => data.user_username,
                :user_first_name => data.user_first_name,
                :user_last_name => data.user_last_name,
                :user_permissions => data.user_permissions,
                :user_arena_location => data.user_arena_location,
                :assignment_jar_path => data.assignment_jar_path,
                :assignment_open_date => data.assignment_open_date,
                :assignment_due_date => data.assignment_due_date,
                :user_level_id => data.user_level_id
           }

  end

  # Chat Message should have:
  # int user_id
  # string user_first_name
  # string user_last_name
  # datetime timestamp
  # string source [blaze:<instance>, forge]
  # string message

  def get_chat
    if not sessionIsLoggedIn
      render :json => { :success => false, :error => 'You must be logged in to use this service.' }
      return
    end

    render :json => { :success => true, :last_50 => $chat_messages }
  end

  def push_chat
    if not sessionIsLoggedIn
      render :json => { :success => false, :error => 'You must be logged in to use this service.' }
      return
    end

    jsonRequest = JSON.parse(request.raw_post)
    if jsonRequest.nil?
      render :json => { :success => false, :error => 'Failed to parse JSON data' }
      return
    end

    $chat_messages.append(jsonRequest['message'])
    render :json => { :success => true }
  end

end