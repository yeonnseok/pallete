import {ReactElement} from 'react';

import {FormProvider, useForm} from 'react-hook-form';
import {
  Button,
  Cascader,
  CascaderProps,
  Checkbox,
  Col,
  Form,
  Input,
  InputNumber,
  Row,
  Select
} from "antd";

export function SignupPage(): ReactElement {
  const form = useForm();

  const handleSubmit = (data: any): void => {
  };

  const {Option} = Select;

  interface DataNodeType {
    value: string;
    label: string;
    children?: DataNodeType[];
  }

  const residences: CascaderProps<DataNodeType>['options'] = [
    {
      value: 'seoul',
      label: '서울특별시',
      children: [
        {
          value: 'yongsan',
          label: '용산구',
          children: [
            {
              value: 'namyoung',
              label: '남영동',
            },
          ],
        },
      ],
    },
    {
      value: 'gyeonggi',
      label: '경기도',
      children: [
        {
          value: 'suwon',
          label: '수원시',
          children: [
            {
              value: 'jangan',
              label: '장안구',
            },
          ],
        },
      ],
    }
  ];

  const formItemLayout = {
    labelCol: {
      xs: {span: 24},
      sm: {span: 8},
    },
    wrapperCol: {
      xs: {span: 24},
      sm: {span: 16},
    },
  };

  const tailFormItemLayout = {
    wrapperCol: {
      xs: {
        span: 24,
        offset: 0,
      },
      sm: {
        span: 16,
        offset: 8,
      },
    },
  };


  const onFinish = (values: any) => {
    console.log('Received values of form: ', values);
  };

  const prefixSelector = (
    <Form.Item name="prefix" noStyle>
      <Select style={{width: 70}}>
        <Option value="82">+82</Option>
      </Select>
    </Form.Item>
  );

  return (
    <FormProvider {...form}>
      <div style={{display: 'flex', justifyContent: 'center', marginTop: '50px'}}>
        <Form
          {...formItemLayout}
          name="register"
          onFinish={onFinish}
          initialValues={{
            residence: ['seoul', '용산구', '남영동'], prefix: '82'
          }}
          style={{maxWidth: 600}}
          scrollToFirstError
        >
          <Form.Item
            name="email"
            label="이메일"
            rules={[
              {
                type: 'email',
                message: 'The input is not valid E-mail!',
              },
              {
                required: true,
                message: 'Please input your E-mail!',
              },
            ]}
          >
            <Input/>
          </Form.Item>

          <Form.Item
            name="password"
            label="비밀번호"
            rules={[
              {
                required: true,
                message: 'Please input your password!',
              },
            ]}
            hasFeedback
          >
            <Input.Password/>
          </Form.Item>

          <Form.Item
            name="confirm"
            label="비밀번호 확인"
            dependencies={['password']}
            hasFeedback
            rules={[
              {
                required: true,
                message: 'Please confirm your password!',
              },
              ({getFieldValue}) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('The new password that you entered do not match!'));
                },
              }),
            ]}
          >
            <Input.Password/>
          </Form.Item>

          <Form.Item
            name="nickname"
            label="닉네임"
            tooltip="What do you want others to call you?"
            rules={[{required: true, message: 'Please input your nickname!', whitespace: true}]}
          >
            <Input/>
          </Form.Item>

          <Form.Item
            name="name"
            label="이름"
            rules={[
              {
                required: true,
                message: '이름을 입력해주세요',
              },
            ]}
          >
            <Input/>
          </Form.Item>

          <Form.Item
            name="age"
            label="나이"
            rules={[
              {
                required: true,
                message: '나이를 입력해주세요.',
              },
              {
                validator: async(_, value) => {
                  if (value < 0) {
                    return Promise.reject(new Error('나이는 0보다 작을 수 없습니다.'));
                  }
                  if (isNaN(value)) {
                    return Promise.reject(new Error('숫자만 입력해주세요.'));
                  }
                },
              },
            ]}
          >
            <InputNumber style={{width: '100%'}}/>
          </Form.Item>

          <Form.Item
            name="residence"
            label="거주지"
            rules={[
              {type: 'array', required: true, message: 'Please select your habitual residence!'},
            ]}
          >
            <Cascader options={residences}/>
          </Form.Item>

          <Form.Item
            name="phone"
            label="휴대폰 번호"
            rules={[{required: true, message: 'Please input your phone number!'}]}
          >
            <Input addonBefore={prefixSelector} style={{width: '100%'}}/>
          </Form.Item>

          <Form.Item
            name="gender"
            label="성별"
            rules={[{required: true, message: 'Please select gender!'}]}
          >
            <Select placeholder="select your gender">
              <Option value="male">남</Option>
              <Option value="female">여</Option>
              <Option value="other">기타</Option>
            </Select>
          </Form.Item>

          <Form.Item
            name="intro"
            label="자기소개"
            rules={[{required: true, message: 'Please input Intro'}]}
          >
            <Input.TextArea showCount maxLength={100}/>
          </Form.Item>

          <Form.Item label="Captcha" extra="We must make sure that your are a human.">
            <Row gutter={8}>
              <Col span={12}>
                <Form.Item
                  name="captcha"
                  noStyle
                  rules={[{required: false, message: 'Please input the captcha you got!'}]}
                >
                  <Input/>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Button>Get captcha</Button>
              </Col>
            </Row>
          </Form.Item>

          <Form.Item
            name="agreement"
            valuePropName="checked"
            rules={[
              {
                validator: (_, value) =>
                  value ? Promise.resolve() : Promise.reject(new Error('Should accept agreement')),
              },
            ]}
            {...tailFormItemLayout}
          >
            <Checkbox>
              I have read the <a href="">agreement</a>
            </Checkbox>
          </Form.Item>
          <Form.Item {...tailFormItemLayout}>
            <Button type="primary" htmlType="submit">
              가입하기
            </Button>
          </Form.Item>
        </Form>
      </div>
    </FormProvider>
  );
}
