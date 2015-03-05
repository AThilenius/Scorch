require 'test_helper'

class ServiceControllerTest < ActionController::TestCase
  test "should get get_minecraft_session" do
    get :get_minecraft_session
    assert_response :success
  end

end
