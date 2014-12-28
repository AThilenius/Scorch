class ServiceController < ApplicationController
  skip_before_filter :verify_authenticity_token, :only => [:get_minecraft_session, :get_user_level_data]

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

    # Check for a pinned or already allocated account
    account = MinecraftAccount.where(:allocated_user_id => user.id).first

    # No account (aka no pinned account)
    if account.nil?
      account = MinecraftAccount.where(:state => :free).first

      # if the account is still nil, it means we are out of accounts
      if account.nil?
        render :json => { :error => 'No free Minecraft accounts available' }
        return
      end

      # Mark the account allocated
      account.state = 'allocated'
      account.allocated_user_id = user.id
      account.save
    end

    # Authenticate with Minecraft servers
    jsonText = {
        "agent" => {
            "name" => "Minecraft",
            "version" => 1
        },
        "username" => "#{account.username}",
        "password" => "#{account.password}"
    }.to_json

    uri = URI.parse("https://authserver.mojang.com/authenticate")
    https = Net::HTTP.new(uri.host,uri.port)
    https.use_ssl = true
    req = Net::HTTP::Post.new(uri.path, initheader = {'Content-Type' =>'application/json'})
    req.body = jsonText
    res = https.request(req)
    data_parsed = JSON.parse(res.body)
    userType = ''
    if data_parsed['selectedProfile']['legacy']
      userType = ' --userType legacy'
    end

    # Return the compiled user_args to them
    render :json => { :user_args =>
                          "--username #{account.username}" +
                              " --uuid #{data_parsed['selectedProfile']['id']}" +
                              " --accessToken #{data_parsed['accessToken']}" +
                              " --userProperties {}" +
                              userType }
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


end