import {ReactElement} from 'react';

import {FormProvider, useForm} from 'react-hook-form';
import {Button, Checkbox, Form, Input} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";

export function LoginPage(): ReactElement {
  const form = useForm();

  const handleSubmit = (data: any): void => {
    // call api
  };

  const onFinish = (values: any) => {
    console.log('Success:', values);
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log('Failed:', errorInfo);
  };

  type FieldType = {
    email?: string;
    password?: string;
    remember?: string;
  };

  return (
    <FormProvider {...form}>
      <div style={{display: 'flex', justifyContent: 'center', marginTop: '200px'}}>
        <Form
          name="normal_login"
          className="login-form"
          style={{minWidth: 300}}
          initialValues={{remember: true}}
          onFinish={onFinish}
        >
          <Form.Item<FieldType>
            name="email"
            rules={[{required: true, message: '이메일을 입력해주세요'}]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="email" />
          </Form.Item>

          <Form.Item<FieldType>
            name="password"
            rules={[{required: true, message: '비밀번호를 입력해주세요'}]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Password"
            />
          </Form.Item>

          <Form.Item<FieldType>
            name="remember"
            valuePropName="checked"
          >
            <Checkbox>로그인 정보 기억하기</Checkbox>

            <a className="login-form-forgot" href="" style={{float: 'right'}}>
              비밀번호 찾기
            </a>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" className="login-form-button" style={{width: '100%'}}>
              로그인
            </Button>
            Or <a href="">회원가입!</a>
          </Form.Item>
        </Form>
      </div>
    </FormProvider>
  );
}

