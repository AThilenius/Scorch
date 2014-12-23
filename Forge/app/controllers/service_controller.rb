class ServiceController < ApplicationController
  skip_before_filter :verify_authenticity_token, :only => [:get_minecraft_session]

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

    # Return the compiled user_args to them
    render :json => { :user_args =>
                          "--username #{account.username}" +
                              " --uuid #{account.uuid}" +
                              " --accessToken #{account.access_token}" +
                              " --userProperties {}" +
                              " --userType #{account.user_type}" }
  end
end