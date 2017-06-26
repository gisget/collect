import { CliStablePage } from './app.po';

describe('open-foris-collect App', () => {
  let page: OpenForisCollectPage;

  beforeEach(() => {
    page = new OpenForisCollectPage();
  });

  it('should display message Open Foris Collect', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Open Foris Collect');
  });
});